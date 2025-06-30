//result_tVO
package pm.vo;

public class result_tVO {
    private String ques_num, test_idx, stdno, result_t_idx, score;

    private String test_name, lec_no, std_name;

    public String getQues_num() {
        return ques_num;
    }

    public void setQues_num(String ques_num) {
        this.ques_num = ques_num;
    }

    public String getTest_idx() {
        return test_idx;
    }

    public void setTest_idx(String test_idx) {
        this.test_idx = test_idx;
    }

    public String getStdno() {
        return stdno;
    }

    public void setStdno(String stdno) {
        this.stdno = stdno;
    }

    public String getResult_t_idx() {
        return result_t_idx;
    }

    public void setResult_t_idx(String result_t_idx) {
        this.result_t_idx = result_t_idx;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getLec_no() {
        return lec_no;
    }

    public void setLec_no(String lec_no) {
        this.lec_no = lec_no;
    }

    public String getStd_name() {
        return std_name;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
    }

    @Override
    public String toString() {
        return "result_tVO{" +
                "ques_num='" + ques_num + '\'' +
                ", test_idx='" + test_idx + '\'' +
                ", stdno='" + stdno + '\'' +
                ", result_t_idx='" + result_t_idx + '\'' +
                ", score='" + score + '\'' +
                ", test_name='" + test_name + '\'' +
                ", lec_no='" + lec_no + '\'' +
                '}';
    }
}
