package imgqueue

import java.io.File
import java.nio.file.{Files, StandardCopyOption}
import javax.servlet.MultipartConfigElement
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.websocket._
import javax.websocket.server.ServerEndpoint

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.server.handler.{AbstractHandler, ContextHandler, ContextHandlerCollection, ResourceHandler}
import org.eclipse.jetty.server.{Handler, Request, Server}
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer

import scala.collection.JavaConverters

object Starter extends App with LazyLogging {
  var wscontainer: ServerContainer = null
  val server = new Server(9988)
  server
  val resource_handler = new ResourceHandler
  resource_handler.setDirectoriesListed(true)
  resource_handler.setWelcomeFiles(Array[String]("index.html"))
  val path: File = new File(".").getAbsoluteFile
  val tmp = new File(path, "tmp")
  resource_handler.setResourceBase(path.getAbsolutePath)

  val x = new AbstractHandler {

    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {

      baseRequest.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT,
        new MultipartConfigElement(tmp.getAbsolutePath))
      if (request.getMethod == "POST") {
        val part = request.getPart("fileToUpload")
        val latest = path.toPath.resolveSibling("bert.jpg")
        Files.copy(part.getInputStream, latest, StandardCopyOption.REPLACE_EXISTING)
      }
      JavaConverters.asScalaSet(wscontainer.getOpenSessions).map(_.getAsyncRemote)
        .foreach(in ⇒ {
          in.sendText("reload")
        })
      response.sendRedirect("/")
      baseRequest.setHandled(true)
    }
  }

  def contexts(aiii: (Handler, String)*): ContextHandlerCollection = {
    val contexts = new ContextHandlerCollection

    val chs = aiii.map(in ⇒ {
      val ch = new ContextHandler(in._2)
      ch.setHandler(in._1)
      ch
    })
    contexts.setHandlers(chs.toArray)
    contexts
  }

  val ws = new ServletContextHandler(ServletContextHandler.SESSIONS)
  server.setHandler(contexts((ws, "/ws"), (x, "/add"), (resource_handler, ".")))
  wscontainer = WebSocketServerContainerInitializer.configureContext(ws)
  wscontainer.addEndpoint(classOf[EventSocket])
  server.start()

  server.join()

}

@ClientEndpoint
@ServerEndpoint(value = "/events/")
class EventSocket {

}
