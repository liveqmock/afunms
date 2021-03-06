package com.afunms.config.manage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.model.Business;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.User;

@SuppressWarnings("unchecked")
public class BusinessManager extends BaseManager implements ManagerInterface {
	public String delete() {
		String id = getParaValue("id");
		BusinessDao dao = new BusinessDao();
		try {
			dao.deleteVoAndChildVoById(id);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list();
	}

	public String execute(String action) {
		if (action.equals("list")) {
			return list();
		}
		if (action.equals("ready_add")) {
			return "/config/business/add.jsp";
		}
		if (action.equals("add")) {
			Business vo = new Business();
			vo.setName(getParaValue("name"));
			vo.setDescr(getParaValue("descr"));
			vo.setPid(getParaValue("pid"));
			DaoInterface dao = new BusinessDao();
			setTarget("/business.do?action=list");
			String url = save(dao, vo);

			// 将Bussiness业务列表装到内存中
			BusinessDao bussdao = new BusinessDao();
			List buslist = new ArrayList();
			try {
				buslist = bussdao.loadAll();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bussdao.close();
			}
			if (buslist != null && buslist.size() > 0) {
				ShareData.setAllbussness(buslist);
			}

			return url;
		}
		if (action.equals("delete")) {
			String url = delete();
			// 将Bussiness业务列表装到内存中
			BusinessDao bussdao = new BusinessDao();
			List buslist = new ArrayList();
			try {
				buslist = bussdao.loadAll();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bussdao.close();
			}
			if (buslist != null && buslist.size() > 0) {
				ShareData.setAllbussness(buslist);
			}

			return url;
		}
		if (action.equals("update")) {
			Business vo = new Business();
			vo.setId(getParaValue("id"));
			vo.setName(getParaValue("name"));
			vo.setDescr(getParaValue("descr"));
			vo.setPid(getParaValue("pid"));

			DaoInterface dao = new BusinessDao();
			setTarget("/business.do?action=list");
			String url = update(dao, vo);

			// 将Bussiness业务列表装到内存中
			BusinessDao bussdao = new BusinessDao();
			List buslist = new ArrayList();
			try {
				buslist = bussdao.loadAll();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bussdao.close();
			}
			if (buslist != null && buslist.size() > 0) {
				ShareData.setAllbussness(buslist);
			}

			return url;
		}
		if (action.equals("ready_edit")) {
			DaoInterface dao = new BusinessDao();
			setTarget("/config/business/edit.jsp");
			return readyEdit(dao);
		}
		if ("setBid".equals(action)) {
			return setBid();
		}
		if ("setBidbyuser".equals(action)) {
			return setBidByUser();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

	public String list() {
		List list = null;
		BusinessDao dao = new BusinessDao();
		try {
			list = dao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("list", list);
		return "/config/business/list.jsp";
	}

	public String setBid() {
		BusinessDao businessDao = new BusinessDao();
		List allbusiness = null;
		try {
			allbusiness = businessDao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("allbusiness", allbusiness);

		String value = getParaValue("value");
		List bidIsSelected = new ArrayList();
		if (value != null) {
			String[] bids = value.split(",");
			if (bids != null && !"".equals(bids)) {
				for (int i = 0; i < bids.length; i++) {
					bidIsSelected.add(bids[i]);
					// System.out.println(bids[i]);
				}
			}
		}
		request.setAttribute("bidIsSelected", bidIsSelected);

		request.setAttribute("event", getParaValue("event"));

		request.setAttribute("eventText", getParaValue("eventText"));
		return "/config/business/setbid.jsp";
	}

	public String setBidByUser() {
		UserDao userDao = new UserDao();
		HttpSession session = request.getSession();
		User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER);

		try {
			vo = userDao.loadAllByUser(vo.getUserid());
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		List alluser = new ArrayList();
		String usr_bid = vo.getBusinessids();

		if (usr_bid != null) {
			String[] user_bid = usr_bid.split(",");
			if (user_bid != null) {
				for (int i = 0; i < user_bid.length; i++) {
					if (user_bid[i].trim().length() > 0) {
						alluser.add(user_bid[i]);
					}
				}
			}
		}
		request.setAttribute("alluser", alluser);

		String value = getParaValue("value");
		List bidIsSelected = new ArrayList();
		if (value != null) {
			String[] bids = value.split(",");
			if (bids != null) {
				for (int i = 0; i < bids.length; i++) {
					bidIsSelected.add(bids[i]);
					System.out.println(bids[i]);
				}
			}
		}
		request.setAttribute("bidIsSelected", bidIsSelected);
		request.setAttribute("event", getParaValue("event"));

		request.setAttribute("eventText", getParaValue("eventText"));
		return "/config/business/setbidbyusr.jsp";
	}
}
