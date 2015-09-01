/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.util.Comparator;

/**
 *
 * @author sergios
 */
public class CustomComparator implements Comparator<User> {
    @Override
        public int compare(User o1, User o2) {
            return o1.getId().compareTo(o2.getId());
    }
}