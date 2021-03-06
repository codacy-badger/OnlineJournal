package com.group53.controllers;


import com.group53.beans.EntityParameter;
import com.group53.beans.Entity;
import com.group53.beans.Parameter;
import com.group53.dao.EntityParameterDAOImpl;
import com.group53.dao.EntityDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class ParameterController {

    private Long entityId;
    @Autowired
    private EntityParameterDAOImpl entityParameterDAO;
    @Autowired
    private EntityDAOImpl entityDAO;

    @RequestMapping(value = "viewParam", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request){
        entityId = Long.parseLong(request.getParameter("id"));
        List<EntityParameter> paramList = entityParameterDAO.getAllParameters(entityId);
        List<Entity> parameters = entityDAO.getAllByType(Parameter.getParameter_entity_type());
        ModelAndView model = new ModelAndView("param", "list", paramList);
        EntityParameter newEntityParameter = new EntityParameter();
        newEntityParameter.setEntityId(entityId);
        model.addObject("entityParameter", newEntityParameter);
        model.addObject("parameters", parameters);
        return model;
    }

    @RequestMapping(value = "/deleteParam", method = RequestMethod.GET)
    public ModelAndView delete(HttpServletRequest request) {
        Long parameterID = Long.parseLong(request.getParameter("param"));
        entityParameterDAO.deleteParameterDB(entityId, parameterID);
        return new ModelAndView("redirect:/viewParam?id=" + entityId);
    }

    @RequestMapping(value = "/editParam", method = RequestMethod.GET)
    public ModelAndView edit(HttpServletRequest request) {
        Long parameterID = Long.parseLong(request.getParameter("param"));
        EntityParameter newEntityParameter = entityParameterDAO.getParameter(entityId, parameterID);
        List<EntityParameter> paramList = entityParameterDAO.getAllParameters(entityId);
        ModelAndView model = new ModelAndView("param", "list", paramList);
        model.addObject("entityParameter", newEntityParameter);
        List<Entity> parameters = entityDAO.getAllByType(Parameter.getParameter_entity_type());
        model.addObject("parameters", parameters);
        return model;
    }

    @RequestMapping(value = "/saveParam", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute EntityParameter entityParameter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date date = dateFormat.parse(entityParameter.getDateString());
            entityParameter.setDateValue(new Date(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        entityParameterDAO.saveParameterDB(entityParameter);
        return new ModelAndView("redirect:/viewParam?id=" + entityId);
    }
}
