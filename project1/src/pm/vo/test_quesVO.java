package pm.vo;

public class test_quesVO {
    //              문제 번호, 문제 내용, 이 문제가 가지는 문항의 수
    private String ques_idx;

    public String getQues_idx() {
        return ques_idx;
    }

    private String ques_num;
    private String ques_con;
    private String test_item_tnum;
    private String test_ques_pit;

    public String getTest_idx() {
        return test_idx;
    }

    public void setTest_idx(String test_idx) {
        this.test_idx = test_idx;
    }

    private String test_idx;

    public String getTest_ques_pit() {
        return test_ques_pit;
    }

    public void setTest_ques_pit(String test_ques_pit) {
        this.test_ques_pit = test_ques_pit;
    }

    public void setQues_idx(String ques_idx) {
        this.ques_idx = ques_idx;
    }

    public String getQues_num() {
        return ques_num;
    }

    public void setQues_num(String ques_num) {
        this.ques_num = ques_num;
    }

    public String getQues_con() {
        return ques_con;
    }

    public void setQues_con(String ques_con) {
        this.ques_con = ques_con;
    }

    public String getTest_item_tnum() {
        return test_item_tnum;
    }

    public void setTest_item_tnum(String test_item_tnum) {
        this.test_item_tnum = test_item_tnum;
    }
}

