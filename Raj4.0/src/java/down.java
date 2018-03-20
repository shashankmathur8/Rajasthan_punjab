import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.*;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 
public class down extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; 
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; 
 
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }
 
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        String uploadPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        
 
        try {
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
    Iterator iter=formItems.iterator();
    String fileName2="",name="",value="";
    while(iter.hasNext()){
        FileItem item=(FileItem)iter.next();
        if (item.isFormField()) {
            
    name = item.getFieldName();
    value = item.getString();
    } else {
    fileName2 = item.getName();
    
    }
    
    }
    
    
    
    
    
    File f = new File(fileName2);
    String hash= Hash.generateSHA256(f);
   hash= Hash.generateSHA256(f);
    
    
    
    MongoClient client=new MongoClient("localhost");
    DB db= client.getDB("imaginex");
    DBCollection collection = db.getCollection("data");
    BasicDBObject wq=new BasicDBObject();
    wq.put("hash", hash);
    DBCursor cur=collection.find(wq);
    if(!cur.hasNext()){
    DBObject inst=new  BasicDBObject();
    inst.put("hash", hash);
    collection.insert(inst);
    if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        
                        item.write(storeFile);
                        request.setAttribute("message",
                            "Upload has been done successfully!"+hash+name+value);
                    }
                }
            }
    
    }else{
    
    request.setAttribute("message",
                            "File Already Exist!");
    
    
    }
       
          
        } catch (Exception ex) {
            request.setAttribute("message",
                    "There was an error: " + ex.getMessage());
        }
        // redirects client to message page
        getServletContext().getRequestDispatcher("/newjsp1.jsp").forward(
                request, response);
    }
}