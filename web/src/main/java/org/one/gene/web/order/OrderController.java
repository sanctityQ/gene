package org.one.gene.web.order;

import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;
import org.springframework.web.multipart.MultipartFile;

@Path
public class OrderController {

    @Post("upload")
    public void upload(@Param("customerCode") String customerCode, @Param("file") MultipartFile[] files, Invocation inv) {

        for (MultipartFile multipartFile : files) {

        }
    }

    public Reply test(){
        return Replys.with("111").as(Text.class);
    }

}
