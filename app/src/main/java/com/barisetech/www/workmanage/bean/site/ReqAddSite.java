package com.barisetech.www.workmanage.bean.site;



import java.util.List;

public class ReqAddSite {

    private String Operation;
    private List<SiteBean> siteBeans;

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public List<SiteBean> getSiteBeans() {
        return siteBeans;
    }

    public void setSiteBeans(List<SiteBean> siteBeans) {
        this.siteBeans = siteBeans;
    }
}
