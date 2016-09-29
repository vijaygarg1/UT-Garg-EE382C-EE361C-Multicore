class RegularBoolean {
    boolean prev; // not shared
    SafeBoolean value;
    public boolean getValue() {
        return value.getValue();
    }
    public void setValue(boolean b) {
        if (prev != b) {
            value.setValue(b);
            prev = b;
        }
    }
}

