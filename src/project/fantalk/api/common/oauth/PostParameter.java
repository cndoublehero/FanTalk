package project.fantalk.api.common.oauth;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import project.fantalk.api.Utils;




/**
 * A data class representing HTTP Post parameter
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class PostParameter implements java.io.Serializable, Comparable {
    String name;
    String value;
    private File file = null;
    
    private static final long serialVersionUID = -8708108746980739212L;

    public PostParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public PostParameter(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public PostParameter(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public PostParameter(String name, File file) {
        this.name = name;
        this.file = file;
    }
    
    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }

    public File getFile() {
        return file;
    }

    public boolean isFile(){
        return null != file;
    }
    
    private static final String JPEG = "image/jpeg";
    private static final String GIF = "image/gif";
    private static final String PNG = "image/png";
    private static final String OCTET = "application/octet-stream";

    /**
     * 
     * @return content-type
     */
    public String getContentType() {
        if (!isFile()) {
            throw new IllegalStateException("not a file");
        }
        String contentType;
        String extensions = file.getName();
        int index = extensions.lastIndexOf(".");
        if (-1 == index) {
            // no extension
            contentType = OCTET;
        } else {
            extensions = extensions.substring(extensions.lastIndexOf(".") + 1).toLowerCase();
            if (extensions.length() == 3) {
                if ("gif".equals(extensions)) {
                    contentType = GIF;
                } else if ("png".equals(extensions)) {
                    contentType = PNG;
                } else if ("jpg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else if (extensions.length() == 4) {
                if ("jpeg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else {
                contentType = OCTET;
            }
        }
        return contentType;
    }
    
    
    public static boolean containsFile(PostParameter[] params) {
        boolean containsFile = false;
        if(null == params){
            return false;
        }
        for (PostParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }
    /*package*/ static boolean containsFile(List<PostParameter> params) {
        boolean containsFile = false;
        for (PostParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }

    public static PostParameter[] getParameterArray(String name, String value) {
        return new PostParameter[]{new PostParameter(name,value)};
    }
    public static PostParameter[] getParameterArray(String name, int value) {
        return getParameterArray(name,String.valueOf(value));
    }

    public static PostParameter[] getParameterArray(String name1, String value1
            , String name2, String value2) {
        return new PostParameter[]{new PostParameter(name1, value1)
                , new PostParameter(name2, value2)};
    }
    public static PostParameter[] getParameterArray(String name1, int value1
            , String name2, int value2) {
        return getParameterArray(name1,String.valueOf(value1),name2,String.valueOf(value2));
    }
    
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }

    @Override public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof PostParameter) {
            PostParameter that = (PostParameter) obj;
            
            if (file != null ? !file.equals(that.file) : that.file != null)
                return false;
            
            return this.name.equals(that.name) &&
                this.value.equals(that.value);
        }
        return false;
    }
   
    @Override
    public String toString() {
        return "PostParameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", file=" + file +
                '}';
    }
    
    public int compareTo(Object o) {
        int compared;
        PostParameter that = (PostParameter) o;
        compared = name.compareTo(that.name);
        if (0 == compared) {
            compared = value.compareTo(that.value);
        }
        return compared;
    }
    
	public static PostParameter[] parseGetParameters(String str) {
		if (Utils.isEmpty(str))
			return null;
		List<PostParameter> tempParams = new ArrayList<PostParameter>();
		String[] queryStrs = str.split("&");
		try {
			for (String query : queryStrs) {
				String[] split = query.split("=");
				if (split.length == 2) {
					tempParams.add(new PostParameter(URLDecoder.decode(
							split[0], "UTF-8"), URLDecoder.decode(split[1],
							"UTF-8")));
				} else {
					tempParams.add(new PostParameter(URLDecoder.decode(
							split[0], "UTF-8"), ""));
				}
			}
		} catch (UnsupportedEncodingException ignore) {
		}
		return tempParams.toArray(new PostParameter[tempParams.size()]);
	}
}
