package pm.vo;

public class AdminVO {
    private String adno, ad_name, ad_address, ad_phone, ad_email;

    public String getAdno() {
        return adno;
    }

    public void setAdno(String adno) {
        this.adno = adno;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getAd_address() {
        return ad_address;
    }

    public void setAd_address(String ad_address) {
        this.ad_address = ad_address;
    }

    public String getAd_phone() {
        return ad_phone;
    }

    public void setAd_phone(String ad_phone) {
        this.ad_phone = ad_phone;
    }

    public String getAd_email() {
        return ad_email;
    }

    public void setAd_email(String ad_email) {
        this.ad_email = ad_email;
    }

    @Override
    public String toString() {
        return ad_name + " (" + adno + ")"; //강사명 콤보박스에 동명이인 선택 방지를 위해
        // 강사명(강사번호) 형식으로 보이도록 함
    }
}
