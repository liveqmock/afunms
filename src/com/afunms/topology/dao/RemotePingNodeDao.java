package com.afunms.topology.dao;

import java.sql.ResultSet;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.topology.model.RemotePingNode;

@SuppressWarnings("unchecked")
public class RemotePingNodeDao extends BaseDao implements DaoInterface {
	public RemotePingNodeDao() {
		super("nms_remote_ping_node");
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		RemotePingNode vo = new RemotePingNode();
		try {
			vo.setId(rs.getInt("id"));
			vo.setNode_id(rs.getString("node_id"));
			vo.setChildNodeId(rs.getString("child_node_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	public List findByNodeId(String nodeId) {
		return findByCriteria("select * from nms_remote_ping_node where node_id='" + nodeId + "'");
	}

	public boolean save(BaseVo vo) {
		RemotePingNode remotePingNode = (RemotePingNode) vo;
		StringBuffer sql = new StringBuffer(200);
		sql.append("insert into nms_remote_ping_node(node_id,child)" + "values('");
		sql.append(remotePingNode.getNode_id());
		sql.append("','");
		sql.append(remotePingNode.getChildNodeId());
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}

	public boolean saveList(List list) {
		boolean result = false;
		try {
			String sql = "insert into nms_remote_ping_node(node_id,child_node_id) values('";
			for (int i = 0; i < list.size(); i++) {
				RemotePingNode remotePingNode = (RemotePingNode) list.get(i);
				String sql2 = sql + remotePingNode.getNode_id() + "','" + remotePingNode.getChildNodeId() + "')";

				conn.addBatch(sql2);
			}
			conn.executeBatch();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		} finally {
			conn.close();
		}
		return result;
	}

	public boolean deleteByNodeId(String nodeId) {
		String sql = "delete from nms_remote_ping_node where node_id='" + nodeId + "'";
		return saveOrUpdate(sql);
	}

	public boolean deleteByChildNodeId(String nodeId) {
		String sql = "delete from nms_remote_ping_node where child_node_id='" + nodeId + "'";
		return saveOrUpdate(sql);
	}

	public boolean update(BaseVo vo) {
		return false;
	}

}
