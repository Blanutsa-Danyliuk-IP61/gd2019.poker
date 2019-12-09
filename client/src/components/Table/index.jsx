import React from 'react';

import { Grid } from '@material-ui/core';
import Card from '../Card';

import useStyles from './styles';

const Table = (props) => {

    const classes = useStyles();
    const { cards } = props;

    return (
          <Grid container className={classes.root} justify='space-between'>
              {cards.map(card => (
                  <Grid
                      key={card.short + card.suitEmoji}
                      item
                      container
                      xs={2}
                  >
                    <Card shown card={card} />
                  </Grid>
              ))}
          </Grid>
    );
};

export default Table;
