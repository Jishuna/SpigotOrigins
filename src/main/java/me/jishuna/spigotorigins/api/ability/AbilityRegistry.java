package me.jishuna.spigotorigins.api.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.jishuna.commonlib.utils.ReflectionUtils;
import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.RegisterAbility;

public class AbilityRegistry {
	private static final Class<?> TYPE_CLASS = Ability.class;

	private final Map<String, Class<? extends Ability>> abilityMap = new HashMap<>();

	public AbilityRegistry() {
		reloadAbilities();
	}

	// Pretty sure this is not an unchecked cast
	@SuppressWarnings("unchecked")
	public void reloadAbilities() {
		this.abilityMap.clear();

		for (Class<?> clazz : ReflectionUtils.getAllClassesInSubpackages("me.jishuna.spigotorigins.api.ability",
				this.getClass().getClassLoader())) {
			if (!TYPE_CLASS.isAssignableFrom(clazz))
				continue;

			for (RegisterAbility annotation : clazz.getAnnotationsByType(RegisterAbility.class)) {
				registerAbility(annotation.name(), (Class<? extends Ability>) clazz);
			}
		}
	}
	
	public void registerAbility(String name, Class<? extends Ability> clazz) {
		this.abilityMap.put(name, clazz);
	}

	public Set<String> getAbilities() {
		return this.abilityMap.keySet();
	}

	public Ability parseString(String string) throws InvalidOriginException {
		int open = string.indexOf('(');
		int close = string.lastIndexOf(')');

		if (open < 0 || close < 0)
			return null;

		String type = string.substring(0, open).toLowerCase();

		String data = string.substring(open + 1, close);

		Class<? extends Ability> clazz = this.abilityMap.get(type);

		if (clazz == null)
			throw new InvalidOriginException("The ability type \"" + type + "\" was not found.");

		Ability ability;
		try {
			ability = clazz.getDeclaredConstructor(String[].class).newInstance((Object) data.split(","));
		} catch (ReflectiveOperationException | IllegalArgumentException e) {
			if (e.getCause() instanceof InvalidOriginException ex) {
				ex.addAdditionalInfo("Error parsing ability \"" + type + "\":");
				throw ex;
			}
			throw new InvalidOriginException("Unknown error: " + e.getMessage());
		}
		System.err.println(ability);
		return ability;
	}

}
