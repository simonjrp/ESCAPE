package se.chalmers.dat255.group22.escape;

import java.util.List;

import se.chalmers.dat255.group22.escape.objects.ListObject;

public abstract class IAutoGenerator {
	
	public IAutoGenerator() {
	}
	
	public abstract List<ListObject> generate();
	
}
