package net.tool.practical.filemanagerplus.free.cosyfile.pojo;

public class ZipPojo {
    private String zipPath;
    private String zipSize;
    private String zipTime;
    private String zipName;
    private Boolean isSelect = false;

    public ZipPojo(String zipPath, String zipSize, String zipTime, String zipName) {
        this.zipPath = zipPath;
        this.zipSize = zipSize;
        this.zipTime = zipTime;
        this.zipName = zipName;
    }

    public String getZipSize() {
        return zipSize;
    }

    public void setZipSize(String zipSize) {
        this.zipSize = zipSize;
    }

    public String getZipTime() {
        return zipTime;
    }

    public void setZipTime(String zipTime) {
        this.zipTime = zipTime;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
