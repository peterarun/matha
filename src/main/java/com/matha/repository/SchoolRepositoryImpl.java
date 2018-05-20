package com.matha.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.matha.domain.District;
import com.matha.domain.School;
import com.matha.domain.State;

@Repository
public class SchoolRepositoryImpl implements SchoolRepositoryCust{

	@PersistenceContext
	private EntityManager entityManger;

	@SuppressWarnings("unchecked")
	public List<School> fetchSchools(State state, District district, String name, String city, String pin) {

		String qryStr = "from School s ";
		List<String> qryStrArr = new ArrayList<>();
		if (!StringUtils.isBlank(name)) {
			qryStrArr.add("s.name like :school");
		}
		if (!StringUtils.isEmpty(pin)) {
			qryStrArr.add("s.pin like :pin");
		}
		if (state != null) {
			qryStrArr.add("s.state = :state");
		}
		if (district != null) {
			qryStrArr.add("s.district = :district");
		}
		if (!qryStrArr.isEmpty()) {
			qryStr += " where " + qryStrArr.get(0);
			for (int i = 1; i < qryStrArr.size(); i++) {
				qryStr += " and " + qryStrArr.get(i);
			}
		}
		Query crit = entityManger.createQuery(qryStr);
		if (!StringUtils.isBlank(name)) {
			crit.setParameter("school", name + "%");
		}
		if (!StringUtils.isEmpty(pin)) {
			crit.setParameter("pin", pin + "%");
		}
		if (state != null) {
			crit.setParameter("state", state);
		}
		if (district != null) {
			crit.setParameter("district", district);
		}

		List<School> data = crit.getResultList();
		// if (state != null) {
		// crit.add(Restrictions.eq("state", state));
		// }
		// if (district != null) {
		// crit.add(Restrictions.eq("district", district));
		// }
		// if (pin != null) {
		// crit.add(Restrictions.eq("pin", pin));
		// }
		// if (city != null) {
		// crit.add(Restrictions.eq("city", city));
		// }
		// if (name != null) {
		// crit.add(Restrictions.like("name", name + "%"));
		// }

		return data;
	}

	@Override
	public List<School> fetchSchools(int startId) {

		String qryStr = "from School s where id > :id order by id";

		Query crit = entityManger.createQuery(qryStr);
		crit.setParameter("id", startId);

		List<School> data = crit.getResultList();
		return data;
	}
}
