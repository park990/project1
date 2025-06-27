package pm.vo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClassVO {
    private String lec_stdno;
    private String lec_no;
    private String lec_name, adno, lec_info, ad_name, lec_sdate, lec_edate, lec_limit, period;

    public String getLec_stdno() {
        return lec_stdno;
    }

    public void setLec_stdno(String lec_stdno) {
        this.lec_stdno = lec_stdno;
    }

    public String getLec_no() {
        return lec_no;
    }

    public void setLec_no(String lec_no) {
        this.lec_no = lec_no;
    }

    public String getLec_name() {
        return lec_name;
    }

    public void setLec_name(String lec_name) {
        this.lec_name = lec_name;
    }

    public String getAdno() {
        return adno;
    }

    public void setAdno(String adno) {
        this.adno = adno;
    }

    public String getLec_info() {
        return lec_info;
    }

    public void setLec_info(String lec_info) {
        this.lec_info = lec_info;
    }

    public String getLec_sdate() {
        return lec_sdate;
    }

    public void setLec_sdate(String lec_sdate) {
        this.lec_sdate = lec_sdate;
    }

    public String getLec_edate() {
        return lec_edate;
    }

    public void setLec_edate(String lec_edate) {
        this.lec_edate = lec_edate;
    }

    public String getLec_limit() {
        return lec_limit;
    }

    public void setLec_limit(String lec_limit) {
        this.lec_limit = lec_limit;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getPeriod() {

        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    //강의 기간(화면출력용 데이터)
    public String updatePeriod() {
        try {
            if(lec_sdate != null && lec_edate != null)  { //만약 강의시작일과 강의종료일이 null값이 아니면,
                //날짜를 처리하도록 도와주는 함수: DateTimeFormatter
                DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter outputFmt = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

                LocalDate start = LocalDate.parse(lec_sdate, inputFmt);
                LocalDate end = LocalDate.parse(lec_edate, inputFmt);

                return outputFmt.format(start) + " ~ " + outputFmt.format(end);
            }
        }   catch (Exception e){
            //무시하고 아래 공백을 반환

        }
        return "";

    }




}
