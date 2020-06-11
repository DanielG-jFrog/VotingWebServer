import com.sun.net.httpserver.HttpServer

int PORT = 3000
String filePath = "./votes.json"


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
    createContext("/") { http ->
        http.responseHeaders.add("Content-type", "text/plain")
        http.sendResponseHeaders(200, 0)
        http.responseBody.withWriter { out ->
            out << "Your vote is accepted\n"
            String requestBody = http.requestBody.text.toString()
            writeResultsToFile(filePath, requestBody)
        }
        println "Request received"
    }
    start()
}
