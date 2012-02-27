package project.fantalk.api;

public enum ReturnCode {
	ERROR_OK,//没问题
	ERROR_REPETITION,//Twitter，重复Tweet
	ERROR_WORDS_LIMIT_ERROR,//超过140字
	ERROR_SERVER_ERROR,//服务器故障,5XX
	ERROR_FALSE,//失败
	ERROR_NOT_PREPARED;//代码未完成
	;
	
	public boolean isOk() {
		return this.equals(ERROR_OK);
	}
	
	public boolean isPrepared() {
		return this.equals(ERROR_NOT_PREPARED);
	}
}
