package service;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


import controller.serverController;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	
	private serverController cs = new serverController();
    
    @GET
    @Produces("image/jpg")
    @Path("/{zoom}/{x}/{y}/{inf_n}" )
    public File getZoom(@PathParam("zoom") double zoom,
    					@PathParam("x") double x, 
    					@PathParam("y") double y, 
    					@PathParam("inf_n") int inf_n) throws Exception {
        
    	
    	
    	return cs.testController( zoom, x, y , inf_n );
    }
}
