package me.musty.client.setting;

public class Setting {
    private String name;
    private Object value;
    public Setting(String name, Object value) { this.name = name; this.value = value; }
    public String getName() { return name; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
}
