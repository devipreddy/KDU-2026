class ItemPlaceholder {

    public ItemPlaceholder() {
        System.out.println("ALERT: Creating expensive placeholder object!");
    }

    public String getInfo() {
        return "ID-NOT-FOUND: Placeholder Item";
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
