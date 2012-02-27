/**
 * 
 */
package project.fantalk.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author mcxiaoke
 */
public class App implements Serializable {
    private static final long serialVersionUID = 8354523618410107421L;
    /** 黑名单，限制使用FanTalk */
    private List<String> blacklist;

    /**
     * 
     */
    public App() {
    	
    }

}
