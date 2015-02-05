package com.ataulm.wutson.show;

import java.util.Iterator;
import java.util.List;

class Cast implements Iterable<Character> {

    private final List<Character> characters;

    Cast(List<Character> characters) {
        this.characters = characters;
    }

    @Override
    public Iterator<Character> iterator() {
        return characters.iterator();
    }

}
