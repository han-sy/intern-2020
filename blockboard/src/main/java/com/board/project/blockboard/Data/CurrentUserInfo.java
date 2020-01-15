package com.board.project.blockboard.Data;

/**
 * 싱글턴 패턴을 적용하여
 * 현재 유저의 id와 고객사의 id를 관리
 */
public class CurrentUserInfo {
    public static CurrentUserInfo currentUserInfo;
    public String user_id;
    public int com_id;

    private CurrentUserInfo() {
        this.user_id = null;
        this.com_id = -1;
    }

    public static CurrentUserInfo getInstance(){
        if(currentUserInfo ==null){
            currentUserInfo = new CurrentUserInfo();
        }
        return currentUserInfo;
    }

    public int getCom_id() {
        return com_id;
    }

    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
