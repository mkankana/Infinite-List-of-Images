package lazyloading.infinitegridofimages;

public class ImageData {

    private String name;
    private String imageId;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
