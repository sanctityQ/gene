package org.one.gene.web;


import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;

@Path
public class IndexAction {

    @Get("/")
    public  String forward(){
      return "r:/index";
    }

    @Get("login")
    @Post("login")
    public String login() {
        return "login";
    }

    @Get("index")
    public String index() {
        return "index";
    }

}
