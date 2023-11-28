package net.tool.practical.filemanagerplus.free.cosyfile.pojo;

public class ImagePojo {
    private String imagePath;
    private String fileName;
    private Boolean isSelect = false;

    public ImagePojo(String imagePath, String fileName) {
        this.imagePath = imagePath;
        this.fileName = fileName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
