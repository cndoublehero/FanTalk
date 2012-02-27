package project.fantalk.api.tencent;

import project.fantalk.api.IBaseMethod;
import project.fantalk.api.ReturnCode;

public class TencentService implements IBaseMethod {

    public TencentService() {}

    public ReturnCode verify() {
        return ReturnCode.ERROR_OK;
    }

    public ReturnCode update(String text) {
        return ReturnCode.ERROR_OK;
    }

}
