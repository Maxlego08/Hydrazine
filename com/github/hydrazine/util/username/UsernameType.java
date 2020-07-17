package com.github.hydrazine.util.username;

public enum UsernameType {

	RANDOM, NATURAL, CONST

	;

	public static UsernameType fromString(String type) {
		for (UsernameType t : values())
			if (t.name().equalsIgnoreCase(type))
				return t;
		return null;
	}

}
