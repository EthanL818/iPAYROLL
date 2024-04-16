package se2203b.ipayroll;

public class DeductionSource {
    private int code;
    private String name;

    // Constructor
    public DeductionSource(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getters
    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}

