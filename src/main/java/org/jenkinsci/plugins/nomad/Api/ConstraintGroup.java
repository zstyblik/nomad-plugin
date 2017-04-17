package org.jenkinsci.plugins.nomad.Api;
import java.util.*;
import org.jenkinsci.plugins.nomad.NomadConstraintTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConstraintGroup {
	private List<Constraint> constraints = new ArrayList<Constraint>();

	public ConstraintGroup (
		List<NomadConstraintTemplate> constraintTemplate
		) {
		Iterator<NomadConstraintTemplate> constraintIterator = constraintTemplate.iterator();
		while (constraintIterator.hasNext()) {
			NomadConstraintTemplate nextTemplate = constraintIterator.next();
			constraints.add(new Constraint(nextTemplate));
		}
	}

	public List<Constraint> getConstraints() {
		Iterator<Constraint> constraintIterator = constraints.iterator();
		
		List<Constraint> Constraints = new ArrayList<Constraint>();

		while (constraintIterator.hasNext()) {
			Constraint nextConstraint = constraintIterator.next();
			Constraints.add(nextConstraint);
		}
		return Constraints;
	}
}