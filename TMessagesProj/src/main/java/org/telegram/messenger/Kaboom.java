/*
 * This is the source code of Telegram for Android v. 7.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Duress code + KABOOM implementation (inspired by Telegraher & GrapheneOS duress idea).
 * When the duress code (a decoy PIN) is entered on the lock screen, or when the
 * passcode is failed KABOOM_PIN_FAILS times in a row, the app wipes all of its data.
 */

package org.telegram.messenger;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class Kaboom {

    private static String mrHangman(int tries) {
        if (tries >= BuildVars.KABOOM_PIN_FAILS) return "\uD83D\uDCA3";
        if (tries < BuildVars.KABOOM_PIN_FAILS - 6) return "\uD83D\uDC37\uD83D\uDC6E\u200D♂️";
        String[] hangman = new String[]{
                " +--+\n |  |\n    |\n    |\n    |\n    |\n=====",
                " +--+\n |  |\n O  |\n    |\n    |\n    |\n=====",
                " +--+\n |  |\n O  |\n |  |\n    |\n    |\n=====",
                " +--+\n |  |\n O  |\n/|  |\n    |\n    |\n=====",
                " +--+\n |  |\n O  |\n/|\\ |\n    |\n    |\n=====",
                " +--+\n |  |\n O  |\n/|\\ |\n/   |\n    |\n=====",
                " +--+\n |  |\n O  |\n/|\\ |\n/ \\ |\n    |\n====="
        };
        return hangman[tries - BuildVars.KABOOM_PIN_FAILS + 6];
    }

    private static void gimmeRopeAndFindATree(Context context, int tries) {
        try {
            TextView tv = new TextView(context);
            tv.setText(mrHangman(tries));
            tv.setTypeface(Typeface.MONOSPACE);
            tv.setGravity(Gravity.START);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.parseColor("#CC000000"));
            tv.setPadding(24, 16, 24, 16);

            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(tv);
            toast.show();
        } catch (Exception ignore) {}
    }

    /**
     * Called on every wrong passcode attempt (shows the hangman) and wipes all
     * application data (KABOOM!) once the failure limit is reached.
     */
    public static void kaboom(Context context, int fails) {
        if (context == null) {
            return;
        }
        gimmeRopeAndFindATree(context, fails);
        if (fails >= BuildVars.KABOOM_PIN_FAILS) {
            try {
                ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }
}
