/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conn.ConnectionDB;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.CartAdmin;

/**
 *
 * @author Tu Nguyen
 */
public class CartDAOImple implements CartDAO {

    @Override
    public void addCart(Order c) {
        String sql = "INSERT INTO ORDER_USER(ma_nguoi_dung, username_order, diachi_order, sdt_order, ngay_mua, thanh_tien, trang_thai) VALUES (?,?,?,?,?,?,?);";

        PreparedStatement ps;
        try {
            Connection con = ConnectionDB.getConnectionDB();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, c.getMa_nguoi_dung());
            ps.setString(2, c.getUsername_order());
            ps.setString(3, c.getDiachi_order());
            ps.setInt(4, c.getSdt_order());
            ps.setDate(5, c.getNgay_mua());
            ps.setInt(6, c.getThanh_tien());
            ps.setString(7, c.getTrang_thai());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void addOrder(int id_order, int ma_san_pham, int so_luong) {
        String sql = "INSERT INTO ORDER_DETAIL(id_order, ma_san_pham, so_luong) VALUES (?,?,?);";

        PreparedStatement ps;
        try {
            Connection con = ConnectionDB.getConnectionDB();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, id_order);
            ps.setInt(2, ma_san_pham);
            ps.setInt(3, so_luong);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int getIdOrder(int ma_nguoi_dung, String username_order, String diachi_order, int sdt_order) {
        String sql = "SELECT id_order FROM ORDER_USER WHERE((ma_nguoi_dung = '" + ma_nguoi_dung + "') AND (username_order = N'" + username_order + "') AND (diachi_order = N'" + diachi_order + "') AND (sdt_order = '" + sdt_order + "') AND (trang_thai = N'ĐANG XỬ LÝ'));";
        int id_order= 0;
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id_order = rs.getInt("id_order");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id_order;
    }
    
    //public static void main(String[] args) {
    //long millis = System.currentTimeMillis();  
    //java.sql.Date ngay_mua = new java.sql.Date(millis);
    //Cart c = new Order(1, 12, "TV", ngay_mua , 2, 1000, "Đang xử lí");
    //CartDAOImple cartDAO = new CartDAOImple();
    //cartDAO.getListCartAdminCategory(1);
    //cartDAO.countCartCategory(1);
    //cartDAO.getListCartAdminCategory(1);
    //cartDAO.addCart(c);
    //cartDAO.removeCart(6);      
    //System.out.println(cartDAO.countCart(3));
    //System.out.println(productDAO.getList());
    //System.out.println(productDAO.getListByCategory(5));}
    @Override
    public List<Order> getListCart(int id) {
        String sql = "SELECT PRODUCT.ten_san_pham, PRODUCT.ma_san_pham, PRODUCT.ma_loai_san_pham, PRODUCT.hinh_anh_1, ORDER_USER.ngay_mua, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.trang_thai FROM ORDER_USER, PRODUCT, ORDER_DETAIL WHERE((ORDER_DETAIL.ma_san_pham = PRODUCT.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND (ma_nguoi_dung = '" + id + "'));";
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String ten_san_pham = rs.getString("ten_san_pham");
                int ma_loai_san_pham = rs.getInt("ma_loai_san_pham");
                int ma_san_pham = rs.getInt("ma_san_pham");
                String hinh_anh_1 = rs.getString("hinh_anh_1");
                Date ngay_mua = rs.getDate("ngay_mua");
                int so_luong = rs.getInt("so_luong");
                int gia_ban = rs.getInt("gia_ban");
                String trang_thai = rs.getString("trang_thai");
                list.add(new Order(ten_san_pham, ma_san_pham, ma_loai_san_pham, hinh_anh_1, ngay_mua, so_luong, gia_ban, trang_thai));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void removeCart(int id) {
        String sql = "DELETE FROM ORDER_DETAIL WHERE(ma_san_pham = '" + id + "');";
        PreparedStatement ps;
        try {
            Connection con = ConnectionDB.getConnectionDB();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CartAdmin> getListCartAdmin() {
        String sql = "SELECT CART.id_cart, CART.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, CART.so_luong, PRODUCT.gia_ban, CART.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, USER_ACCOUNT.user__name, USER_ACCOUNT.sdt, USER_ACCOUNT.dia_chi, CART.ngay_mua, CART.trang_thai  \n"
                + "FROM USER_ACCOUNT, CART, PRODUCT\n"
                + "WHERE ((CART.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = CART.ma_san_pham));";
        List<CartAdmin> list = new ArrayList<CartAdmin>();
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id_cart = rs.getInt("id_cart");
                int ma_san_pham = rs.getInt("ma_san_pham");
                String ten_san_pham = rs.getString("ten_san_pham");
                int ma_loai_san_pham = rs.getInt("ma_loai_san_pham");
                int so_luong = rs.getInt("so_luong");
                int gia_ban = rs.getInt("gia_ban");
                int thanh_tien = rs.getInt("thanh_tien");
                int ma_nguoi_dung = rs.getInt("ma_nguoi_dung");
                String ten_nguoi_dung = rs.getString("user__name");
                String sdt = rs.getString("sdt");
                String dia_chi = rs.getString("dia_chi");
                Date ngay_mua = rs.getDate("ngay_mua");
                String trang_thai = rs.getString("trang_thai");
                list.add(new CartAdmin(id_cart, ma_san_pham, ten_san_pham, ma_loai_san_pham, so_luong, gia_ban, thanh_tien, ma_nguoi_dung, ten_nguoi_dung, sdt, dia_chi, ngay_mua, trang_thai));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<CartAdmin> getListCartAdminCategory(int id) {
        String sql = "SELECT CART.id_cart, CART.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, CART.so_luong, PRODUCT.gia_ban, CART.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, USER_ACCOUNT.user__name, USER_ACCOUNT.sdt, USER_ACCOUNT.dia_chi, CART.ngay_mua, CART.trang_thai  \n"
                + "FROM USER_ACCOUNT, CART, PRODUCT\n"
                + "WHERE ((CART.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = CART.ma_san_pham) AND ma_loai_san_pham = '" + id + "');";
        List<CartAdmin> list = new ArrayList<CartAdmin>();
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id_cart = rs.getInt("id_cart");
                int ma_san_pham = rs.getInt("ma_san_pham");
                String ten_san_pham = rs.getString("ten_san_pham");
                int ma_loai_san_pham = rs.getInt("ma_loai_san_pham");
                int so_luong = rs.getInt("so_luong");
                int gia_ban = rs.getInt("gia_ban");
                int thanh_tien = rs.getInt("thanh_tien");
                int ma_nguoi_dung = rs.getInt("ma_nguoi_dung");
                String ten_nguoi_dung = rs.getString("user__name");
                String sdt = rs.getString("sdt");
                String dia_chi = rs.getString("dia_chi");
                Date ngay_mua = rs.getDate("ngay_mua");
                String trang_thai = rs.getString("trang_thai");
                list.add(new CartAdmin(id_cart, ma_san_pham, ten_san_pham, ma_loai_san_pham, so_luong, gia_ban, thanh_tien, ma_nguoi_dung, ten_nguoi_dung, sdt, dia_chi, ngay_mua, trang_thai));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<CartAdmin> getListCartAdminCategory(int id, int tuy_chon) {
        String sql = "";
        if(tuy_chon == 0) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND PRODUCT.ma_loai_san_pham = '" + id + "');";
        }
        if(tuy_chon == 1) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND PRODUCT.ma_loai_san_pham = '" + id + "')" 
                    + "ORDER BY PRODUCT.ten_san_pham;";
        }
        if(tuy_chon == 2) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND PRODUCT.ma_loai_san_pham = '" + id + "')"
                    + "ORDER BY ORDER_USER.thanh_tien;";
        }
        if(tuy_chon == 3) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND PRODUCT.ma_loai_san_pham = '" + id + "')"
                    + "ORDER BY ORDER_USER.ngay_mua;";
        }
        if(tuy_chon == 4) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND PRODUCT.ma_loai_san_pham = '" + id + "')"
                    + "ORDER BY ORDER_USER.trang_thai;";
        }
        
        List<CartAdmin> list = new ArrayList<CartAdmin>();
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                    int id_order = rs.getInt("id_order");
                    int ma_san_pham = rs.getInt("ma_san_pham");
                    String ten_san_pham = rs.getString("ten_san_pham");
                    int ma_loai_san_pham = rs.getInt("ma_loai_san_pham");
                    int so_luong = rs.getInt("so_luong");
                    int gia_ban = rs.getInt("gia_ban");
                    int thanh_tien = rs.getInt("thanh_tien");
                    int ma_nguoi_dung = rs.getInt("ma_nguoi_dung");
                    String username_order = rs.getString("username_order");
                    String sdt_order = rs.getString("sdt_order");
                    String diachi_order = rs.getString("diachi_order");
                    Date ngay_mua = rs.getDate("ngay_mua");
                    String trang_thai = rs.getString("trang_thai");
                    list.add(new CartAdmin(id_order, ma_san_pham, ten_san_pham, ma_loai_san_pham, so_luong, gia_ban, thanh_tien, ma_nguoi_dung, username_order, sdt_order, diachi_order, ngay_mua, trang_thai));
                }
                con.close();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<CartAdmin> getListCartAdmin(int tuy_chon) {
        String sql = "";
        if(tuy_chon == 0) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order));";
        }
        if(tuy_chon == 1){
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order))" 
                    + "ORDER BY PRODUCT.ten_san_pham;";
        }
        if(tuy_chon == 2) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order))"
                    + "ORDER BY ORDER_USER.thanh_tien;";
        }
        if(tuy_chon == 3) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order))"
                    + "ORDER BY ORDER_USER.ngay_mua;";
        }
        if(tuy_chon == 4) {
            sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                    "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                    "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order))"
                    + "ORDER BY ORDER_USER.trang_thai;";
        }
        List<CartAdmin> list = new ArrayList<CartAdmin>();
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                    int id_order = rs.getInt("id_order");
                    int ma_san_pham = rs.getInt("ma_san_pham");
                    String ten_san_pham = rs.getString("ten_san_pham");
                    int ma_loai_san_pham = rs.getInt("ma_loai_san_pham");
                    int so_luong = rs.getInt("so_luong");
                    int gia_ban = rs.getInt("gia_ban");
                    int thanh_tien = rs.getInt("thanh_tien");
                    int ma_nguoi_dung = rs.getInt("ma_nguoi_dung");
                    String username_order = rs.getString("username_order");
                    String sdt_order = rs.getString("sdt_order");
                    String diachi_order = rs.getString("diachi_order");
                    Date ngay_mua = rs.getDate("ngay_mua");
                    String trang_thai = rs.getString("trang_thai");
                    list.add(new CartAdmin(id_order, ma_san_pham, ten_san_pham, ma_loai_san_pham, so_luong, gia_ban, thanh_tien, ma_nguoi_dung, username_order, sdt_order, diachi_order, ngay_mua, trang_thai));
                }
                con.close();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return list;
    }

    @Override
    public void editCartAdmin(int id_order, String trang_thai) {
        String sql = "UPDATE ORDER_USER SET trang_thai=N'" + trang_thai + "' WHERE(id_order = '" + id_order + "');";
        PreparedStatement ps;
        try {
            Connection con = ConnectionDB.getConnectionDB();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public CartAdmin getCart(int id) {
        String sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND ORDER_USER.id_order = '" + id + "');";
        CartAdmin ca=null;
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                    int id_order = rs.getInt("id_order");
                    int ma_san_pham = rs.getInt("ma_san_pham");
                    String ten_san_pham = rs.getString("ten_san_pham");
                    int ma_loai_san_pham = rs.getInt("ma_loai_san_pham");
                    int so_luong = rs.getInt("so_luong");
                    int gia_ban = rs.getInt("gia_ban");
                    int thanh_tien = rs.getInt("thanh_tien");
                    int ma_nguoi_dung = rs.getInt("ma_nguoi_dung");
                    String username_order = rs.getString("username_order");
                    String sdt_order = rs.getString("sdt_order");
                    String diachi_order = rs.getString("diachi_order");
                    Date ngay_mua = rs.getDate("ngay_mua");
                    String trang_thai = rs.getString("trang_thai");
                    ca = new CartAdmin(id_order, ma_san_pham, ten_san_pham, ma_loai_san_pham, so_luong, gia_ban, thanh_tien, ma_nguoi_dung, username_order, sdt_order, diachi_order, ngay_mua, trang_thai);
                }
                con.close();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return ca;
    }
    
    @Override
    public List<CartAdmin> searchListCartAdmin(String keyword_cart) {
        if(keyword_cart.equals("")){
            keyword_cart = "1111111111111111111111111111111111111111111";
        }
        keyword_cart = '%' + keyword_cart + '%';
        String sql = "";
        sql = "SELECT ORDER_USER.id_order, ORDER_DETAIL.ma_san_pham, PRODUCT.ten_san_pham, PRODUCT.ma_loai_san_pham, ORDER_DETAIL.so_luong, PRODUCT.gia_ban, ORDER_USER.thanh_tien, USER_ACCOUNT.ma_nguoi_dung, ORDER_USER.username_order, ORDER_USER.sdt_order, ORDER_USER.diachi_order, ORDER_USER.ngay_mua, ORDER_USER.trang_thai  \n" +
                "FROM USER_ACCOUNT, ORDER_USER, ORDER_DETAIL, PRODUCT\n" +
                "WHERE ((ORDER_USER.ma_nguoi_dung = USER_ACCOUNT.ma_nguoi_dung) AND (PRODUCT.ma_san_pham = ORDER_DETAIL.ma_san_pham) AND (ORDER_USER.id_order = ORDER_DETAIL.id_order) AND PRODUCT.ten_san_pham LIKE '" + keyword_cart + "');";
        List<CartAdmin> list = new ArrayList<CartAdmin>();
        try {
            Connection con = ConnectionDB.getConnectionDB();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                    int id_order = rs.getInt("id_order");
                    int ma_san_pham = rs.getInt("ma_san_pham");
                    String ten_san_pham = rs.getString("ten_san_pham");
                    int ma_loai_san_pham = rs.getInt("ma_loai_san_pham");
                    int so_luong = rs.getInt("so_luong");
                    int gia_ban = rs.getInt("gia_ban");
                    int thanh_tien = rs.getInt("thanh_tien");
                    int ma_nguoi_dung = rs.getInt("ma_nguoi_dung");
                    String username_order = rs.getString("username_order");
                    String sdt_order = rs.getString("sdt_order");
                    String diachi_order = rs.getString("diachi_order");
                    Date ngay_mua = rs.getDate("ngay_mua");
                    String trang_thai = rs.getString("trang_thai");
                    list.add(new CartAdmin(id_order, ma_san_pham, ten_san_pham, ma_loai_san_pham, so_luong, gia_ban, thanh_tien, ma_nguoi_dung, username_order, sdt_order, diachi_order, ngay_mua, trang_thai));
                }
                con.close();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return list;
    }
}
