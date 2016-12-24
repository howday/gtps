package com.project.gtps.main;

import com.project.gtps.domain.Device;
import com.project.gtps.domain.User;
import com.project.gtps.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suresh on 12/23/16.
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        User user = new User();
        user.setUserName("howday");
        user.setEmail("sklamgade47@gmail.com");
        user.setIsDelete(0);


        List<Device> devices = new ArrayList<Device>();
        Device d1 = new Device();
        d1.setDeviceId("deviceId1");
        d1.setDeviceName("s5");
        d1.setPlatform("android");

        devices.add(d1);

        Device d2 = new Device();
        d2.setDeviceId("deviceId1");
        d2.setDeviceName("s5");
        d2.setPlatform("android");
        devices.add(d2);

        user.setDevices(devices);



        session.save(user);

        session.getTransaction().commit();
        HibernateUtil.shutdown();

    }
}
