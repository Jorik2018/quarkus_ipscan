package org.isobit.ipscan;

import java.util.Random;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import io.smallrye.mutiny.Uni;


/*Status 	| Name 		| Type 			| Operating system 	| IP 			| NetBIOS name 		| NetBIOS group | Manufacturer 		| MAC address 			| User
alive 	| GRAD02 										| 172.16.1.197 	| GRAD02 			| GRAD 			| Hewlett Packard 	| 2C:44:FD:2E:E6:43    	|
alive 	| SGADF09 										| 172.16.1.242 	| SGADF09 			| SGADF 		| Inventec 			| 00:26:6C:28:B5:38 	|
alive 	| 172.16.2.47 									| 172.16.2.47 	|  					|  				|  					| 50:81:40:9D:23:6C |
alive 	| 172.16.2.58 									| 172.16.2.58 	|  					|  				|  					| 96:5A:D9:58:D7:7E |
alive 	| 172.16.2.64 									| 172.16.2.64 	|  					|  				|  					| E0:70:EA:A4:44:33 |
unknown | 172.16.2.153 									| 172.16.2.153 	|  					|  				|  					| 00:00:00:00:00:00 |
unknown | 172.16.2.154 									| 172.16.2.154 	|  					|  				|  					| 00:00:00:00:00:00 |
unknown | 172.16.2.152 									| 172.16.2.152 	|  					|  				|  					| 00:00:00:00:00:00 |
unknown | 172.16.2.155 									| 172.16.2.155 	|  					|  				|  					| 00:00:00:00:00:00 |
service | 172.16.1.244 | ? | IIS Windows (Microsoft IIS httpd 10.0)
service | 172.16.1.244 | HTTP | IIS Windows (Microsoft IIS httpd 10.0)
service | 172.16.1.244 | ? | Windows
service | 172.16.1.239 | ? | IIS Windows (Microsoft IIS httpd 10.0)
service | 172.16.1.239 | HTTP | IIS Windows (Microsoft IIS httpd 10.0)
service | 172.16.1.239 | ? | Windows
service | 172.16.1.200 | ? | IIS Windows (Microsoft IIS httpd 8.5)
service | 172.16.1.200 | HTTP | IIS Windows (Microsoft IIS httpd 8.5)
service | 172.16.1.200 | ? | Windows
*/

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @GET
    public Object scan(@QueryParam("q") String q) {
		ProcessBuilder processBuilder = new ProcessBuilder();
		File f=new File("C:\\Program Files (x86)\\Advanced IP Scanner\\advanced_ip_scanner_console.exe");
		if(!f.exists())f=new File("D:\\Advanced IP Scanner\\advanced_ip_scanner_console.exe");
		
		processBuilder.command(f.toString(),"/r:"+q.replaceAll("\\s+",""));
		ArrayList ips=new ArrayList();
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line=reader.readLine();
			String[] keys=line.split("[|]");
			if(keys.length>0){
				while ((line = reader.readLine()) != null) {
					if(line.trim().length()>0){
						String[] values=line.split("[|]");
						HashMap ip=new HashMap();
						/*for(int k=0,v=0;k<keys.length&&v<values.length;k++,v++)
							ip.put(keys[k],values[v]);*/
						ip.put("status",values[0]);
						if("service".equals(values[0])){
							ip.put("name",values[1]);
							ip.put("type",values[2]);
							ip.put("operatingSystem",values[3]);
						}else{
							ip.put("name",values[1]);
							ip.put("ip",values[2]);
							ip.put("netBIOSName",values[3]);
							ip.put("netBIOSGroup",values[4]);
							ip.put("manufacturer",values[5]);
							ip.put("macAddress",values[6]);
						}
						ips.add(ip);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ips;
    }
	
	class HelloResponse{
private String message;
    private int code;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HelloResponse other = (HelloResponse) obj;
        if (code != other.code) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HelloResponse [message=" + message + ", code=" + code + "]";
    }
		
	}
	
	private HelloResponse generateResponse() {
        HelloResponse response = new HelloResponse();
        response.code=(new Random().nextInt());
        response.message=("Hello World!");
        return response;
    }
	
	@GET
    @Path("/json/")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<HelloResponse> sayHelloWithJsonReactively() {
        return Uni.createFrom().item(this::generateResponse);
    }
	
	
	
	
}