package com.revature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.IllegalRoleException;
import com.revature.models.LoginObject;
import com.revature.models.User;
import com.revature.services.UserService;
import io.javalin.http.Handler;

public class UserController {

    private ObjectMapper oMap;
    private UserService uServ;

    public UserController(UserService uServ) {
        this.uServ = uServ;
        this.oMap = new ObjectMapper();
    }

    public Handler handleManagerViewAllEmployees = (ctx) -> {
        String loggedIn = (String) ctx.req.getSession().getAttribute("loggedIn");


        if (loggedIn == null) {
            ctx.status(401);
            ctx.result("Must login to view employees");
        } else if (loggedIn != null) {
            int role = Integer.parseInt((String) ctx.req.getSession().getAttribute("role"));

            if (role == 1) {
                ctx.result("Must be a manager");
            } else if (role == 2) {
                ctx.result(oMap.writeValueAsString(uServ.managerViewAllEmployees()));
            } else {
                throw new IllegalRoleException();
            }
        }
    };

    public Handler handleEmployeeUpdateAccountInformation = (ctx) -> {
        User u = oMap.readValue(ctx.body(), User.class);
        String loggedIn = (String) ctx.req.getSession().getAttribute("loggedIn");

        if (loggedIn == null) {
            ctx.status(401);
            ctx.result("Must login to update your profile");
        } else if (loggedIn != null) {
            int role = Integer.parseInt((String) ctx.req.getSession().getAttribute("role"));

            if (role == 1) {
                ctx.result(oMap.writeValueAsString(uServ.employeeUpdateAccountInformation(u)));
            } else if (role == 2) {
                ctx.result("Must be an employee");
            } else {
                throw new IllegalRoleException();
            }
        }
    };

    public Handler handleLogin = (ctx) -> {
        LoginObject lo = oMap.readValue(ctx.body(), LoginObject.class);

        User u = uServ.login(lo.username, lo.password);

        if (u == null) {
            ctx.status(403);
            ctx.result("Username or password was incorrect");
        } else {
            ctx.req.getSession().setAttribute("loggedIn", ""+u.getUserName());
            ctx.req.getSession().setAttribute("role", ""+u.getRole());
            ctx.req.getSession().setAttribute("username", ""+u.getUserName());
            ctx.req.getSession().setAttribute("userID", ""+u.getUserID());
            ctx.result(oMap.writeValueAsString(u));
        }
    };

    public Handler handleLogout = (ctx) -> {
        String loggedIn = (String) ctx.req.getSession().getAttribute("loggedIn");
        String username = (String) ctx.req.getSession().getAttribute("username");

        if (loggedIn == null) {
            ctx.status(401);
            ctx.result("You must first login");
        } else if (loggedIn != null) {
            uServ.logout(username);
            ctx.req.getSession().setAttribute("role", null);
            ctx.req.getSession().setAttribute("loggedIn", null);
            ctx.result("Logged out");
        }
    };
}