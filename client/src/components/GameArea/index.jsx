import React from 'react';
import { useSelector } from "react-redux";

import { Grid, Avatar } from '@material-ui/core';
import PlayerPanel from '../PlayerPanel';
import { InfoMessagesQueue } from '../../util/infoMessagesQueue';
import Table from '../Table';
import OptionPanel from '../OptionsPanel';
import { getBalance } from '../../util/redux/reducers/main';

import useStyles from './styles';

const GameArea = () => {

    const classes = useStyles();

    const state = {
        playerData: {
            player: { id: 'player', active: true, hand: [] },
            ai1: { id: 'ai1', active: true, hand: [] },
            ai2: { id: 'ai2', active: true, hand: [] },
            ai3: { id: 'ai3', active: true, hand: [] },
        },
        tableCards: [],
        playerOptions: { Fold: false, Call: false, Deal: true, 'New Game': false },
        displayAICards: false,
        gameStage: 0,
        playerIsActive: true,
        infoMessages: new InfoMessagesQueue(),
    };

    const playerCards = [
        {
            short: '1',
            suitEmoji: '♣️'
        },
        {
            short: '2',
            suitEmoji: '♥️'
        },
    ];

    const tableCards = [
        {
            short: '1',
            suitEmoji: '♣️'
        },
        {
            short: '2',
            suitEmoji: '♥️'
        },
        {
            short: '3',
            suitEmoji: '♣️'
        },
        {
            short: '4',
            suitEmoji: '♥️'
        },
        {
            short: '5',
            suitEmoji: '♣️'
        },
    ];

    const balance = useSelector(getBalance);

    return (
        <Grid container item xs={12} className={classes.root}>
            <Grid key='player-1' item container xs={12} justify='center'>
                <Grid item xs={3}>
                    <PlayerPanel
                        active={true}
                        folded={false}
                        shown={false}
                        login='Mykola'
                        cards={playerCards}
                        balance={100}
                        current={true}
                    />
                </Grid>
            </Grid>
            <Grid key='player-2-table-player-3' item container xs={12} justify='space-between' className={classes.tableContainer}>
                <Grid key='player-2' item xs={3}>
                    <PlayerPanel
                        active={true}
                        folded={false}
                        shown={false}
                        login='Dima'
                        cards={playerCards}
                        balance={100}
                    />
                </Grid>
                <Grid key='table' item container xs={5} direction='column' justify='center'>
                    <Table cards={tableCards} />
                </Grid>
                <Grid key='player-3' item xs={3}>
                    <PlayerPanel
                        active={true}
                        folded={false}
                        shown={false}
                        login='Roma'
                        cards={playerCards}
                        balance={100}
                    />
                </Grid>
            </Grid>
            <Grid key='you' item container xs={12} justify='flex-end'>
                <Grid item xs={4}>
                    <PlayerPanel
                        active={true}
                        folded={false}
                        shown={true}
                        login='Mykola'
                        cards={playerCards}
                        balance={balance}
                    />
                </Grid>
                <Grid key='player-dashboard' container item xs={4} justify='flex-end'>
                    <Grid key='player-dashboard' item xs={9}>
                        <OptionPanel/>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    );
};

export default GameArea;