package net.bytecms.core.utils;

import java.util.ArrayList;
import java.util.List;

public class BuildTree {

	public static <T> Tree<T> build(List<Tree<T>> nodes,boolean ... isck ) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
		for (Tree<T> children : nodes) {
			String pid = children.getParentId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);
				continue;
			}
			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setHasChildren(true);
					continue;
				}
			}

		}
		Tree<T> root = new Tree<T>();
		if (topNodes.size() == 1) {
			root = topNodes.get(0);
		} else {
			root.setId("-1");
			root.setParentId("0");
			root.setHasParent(false);
			root.setHasChildren(true);
			root.setChildren(topNodes);
			root.setName("全 部");
			if(isck.length>0&&isck[0]) {
				root.setChecked(true);
			}
		}
		return root;
	}

	public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
		for (Tree<T> children : nodes) {
			String pid = children.getParentId();
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);
				continue;
			}
			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setHasChildren(true);
					continue;
				}
			}
		}
		return topNodes;
	}
}