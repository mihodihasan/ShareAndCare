package bd.ac.mist.sharecare;

public class Details {
    String post,user,time;

    public Details(String post, String username,String time) {
        this.time = time;
        this.post = post;
        this.user = username;
    }

    public String getPost() {
        return post;
    }


    public String getUser() {
        return user;
    }


    public String getTime() {
        return time;
    }

}
