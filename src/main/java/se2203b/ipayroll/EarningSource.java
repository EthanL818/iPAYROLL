package se2203b.ipayroll;

public class EarningSource {
    private int code;
    private String name;

    public EarningSource(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public EarningSource() {};

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
