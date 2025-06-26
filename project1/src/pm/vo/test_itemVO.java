package pm.vo;

public class test_itemVO {
    //              문제의 인덱스, 문항 번호, 문항 내용(변수랑 데이터베이스컬럼명 변경해야함 ques_con->item_con)
    private String ques_idx, test_item_num, ques_con;

    public String getQues_idx() {
        return ques_idx;
    }

    public void setQues_idx(String ques_idx) {
        this.ques_idx = ques_idx;
    }

    public String getTest_item_num() {
        return test_item_num;
    }

    public void setTest_item_num(String test_item_num) {
        this.test_item_num = test_item_num;
    }

    public String getQues_con() {
        return ques_con;
    }

    public void setQues_con(String ques_con) {
        this.ques_con = ques_con;
    }
}

