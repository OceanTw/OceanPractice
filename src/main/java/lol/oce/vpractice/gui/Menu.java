package lol.oce.vpractice.gui;

import lol.oce.vpractice.gui.impl.DuelMenu;
import lol.oce.vpractice.gui.impl.QueueMenu;
import lol.oce.vpractice.gui.impl.QueueRankedMenu;
import lol.oce.vpractice.gui.impl.QueueUnrankedMenu;
import lombok.Getter;

@Getter
public class Menu {
    QueueMenu queueMenu = new QueueMenu();
    QueueUnrankedMenu queueUnrankedMenu = new QueueUnrankedMenu();
    QueueRankedMenu queueRankedMenu = new QueueRankedMenu();
    DuelMenu duelMenu = new DuelMenu();
}
