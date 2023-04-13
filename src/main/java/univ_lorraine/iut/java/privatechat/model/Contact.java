package univ_lorraine.iut.java.privatechat.model;

import java.io.Serializable;

public class Contact implements Serializable {

    private String pseudo;
    private String ip;
    private Integer port;

    public Contact(String pseudo, String ip, Integer port) {
        this.pseudo = pseudo;
        this.ip = ip;
        this.port = port;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "pseudo='" + pseudo + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}