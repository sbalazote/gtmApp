package com.lsntsolutions.gtmApp.constant;

public enum State {

	ENTERED(1), AUTHORIZED(2), PRINTED(3), ASSEMBLED(4), DELIVERY_NOTE_PRINTED(5), INVOICED(6), CANCELED(7);

	private Integer id;

	State(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public static State fromId(Integer id) {
		for (State state : State.values()) {
			if (state.getId().equals(id)) {
				return state;
			}
		}
		return null;
	}

}
