import React, { Fragment } from 'react';
import coins from '../../assets/images/coins.png';
import { Avatar } from '@material-ui/core';
import useStyles from './styles';

const Coins = () => {

    const classes = useStyles();

    return (
        <Fragment>
            <Avatar src={coins} className={classes.root}/>
        </Fragment>
    )
};

export default Coins;