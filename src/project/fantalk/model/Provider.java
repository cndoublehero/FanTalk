/**
 * 
 */
package project.fantalk.model;

/**
 * 微博/SNS服务提供商
 * @author mcxiaoke
 */
public enum Provider {
    FANFOU("fanfou"), TWITTER("twitter"), BUZZ("buzz"), DOUBAN("douban"), QQ(
            "qq"), SINA("sina"), NETEASE("163"), ZUOSA("zuosa"), FOLLOW5(
            "follow5"), DIGU("digu"), RENREN("renren"), ;

    private String name;

    Provider(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
