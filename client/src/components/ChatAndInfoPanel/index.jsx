import React from 'react';

import { Grid } from '@material-ui/core';
import InfoPanel from '../../containers/InfoPanel';
import Chat from '../../containers/Chat';

import useStyles from './styles';

const ChatAndInfoPanel = () => {

    const classes = useStyles();

    return (
        <Grid
            container
            item
            xs={12}
            className={classes.root}
            justify='space-around'
        >
            <Grid
                key='info-panel'
                item
                xs={5}
                className={classes.item}
            >
                <InfoPanel/>
            </Grid>
            <Grid
                key='char'
                item
                xs={5}
                className={classes.item}
            >
                <Chat/>
            </Grid>
        </Grid>
    );
};

export default ChatAndInfoPanel;