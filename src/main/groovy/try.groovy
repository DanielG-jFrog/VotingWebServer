import com.sun.net.httpserver.HttpServer
import groovy.json.*

int PORT = 3000

def requestBody = [:]

def jsonObject = null

def jsonSlurper = new JsonSlurper()

def file = new File("./votes.json")
//def resutlsFile = jsonSlurper.parse(new File("./votes.json"))

//def jsonSlurper = new JsonSlurper()
//def jsonObject = jsonSlurper.parseText(requestBody)



def writeResultsToFile(String Path, def JsonSlurper){
    def file = new File(Path)
    boolean isExists =  file.exists()
    if(isExists) {
        file.write(JsonSlurper)
    }
    else {
        new File(Path).append(JsonSlurper)
    }
}


HttpServer.create(new InetSocketAddress(PORT), /*max backlog*/ 0).with {
    println "Server is listening on ${PORT}, hit Ctrl+C to exit."
    createContext("/vote") { http ->
        http.responseHeaders.add("Content-type", "text/html")
        http.sendResponseHeaders(200, 0)
        http.responseBody.withWriter { out ->
            out << "Your vote is accepted"
            requestBody = http.requestBody.text.toString()
            jsonObject = jsonSlurper.parseText(requestBody)
            String candidateInRequest = jsonObject.getAt('candidateName')
            println(candidateInRequest)
        }
         def json = JsonOutput.toJson(jsonObject)
         new File("./votes.json").write(json)
         println ("${http.requestMethod.toString()} Request received")
    }

    createContext("/candidates") { http ->
        http.responseHeaders.add("Content-type", "application/json")
        http.sendResponseHeaders(200, 0)
        http.responseBody.withWriter { out ->
            def resutlsFile = jsonSlurper.parse(new File("./votes.json"))
            out << resutlsFile
        }
        println ("${http.requestMethod.toString()} Request received")
    }
    start()
}
