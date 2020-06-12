import com.sun.net.httpserver.HttpServer
import groovy.json.*

int PORT = 3000

def jsonSlurper = new JsonSlurper()
resutlsFile = jsonSlurper.parse(new File("./votes.json"))


def writeResultsToFile(String Path, String textToWrite){
    def file = new File(Path)
    boolean isExists =  file.exists()
    if(isExists) {
        file.write(textToWrite)
    }
    else {
        new File(Path).write(textToWrite)
    }
}


HttpServer.create(new InetSocketAddress(PORT), /*max backlog*/ 0).with {
    println "Server is listening on ${PORT}, hit Ctrl+C to exit."
    createContext("/vote") { http ->
        http.responseHeaders.add("Content-type", "text/html")
        http.sendResponseHeaders(200, 0)
        http.responseBody.withWriter { out ->
            out << "Your vote is accepted\n"
            String requestBody = http.requestBody.text.toString()
            writeResultsToFile("./votes.json", requestBody)
        }
        println ("${http.requestMethod.toString()} Request received")
    }

    createContext("/candidates") { http ->
        http.responseHeaders.add("Content-type", "text/html")
        http.sendResponseHeaders(200, 0)
        http.responseBody.withWriter { out ->
            out << resutlsFile
        }
        println ("${http.requestMethod.toString()} Request received")
    }
    start()
}