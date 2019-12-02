import React from 'react';

import PlayerHand from '../PlayerHand';
import OptionsPanel from '../OptionsPanel/OptionsPanel';

import './styles.css';

const PlayerDashboard = ({ data, options, callbacks }) => {

  return (
    <div className='dashboard'>
        <div id='status-wrapper'>
            <h4>{`Player${data.active ? '' : ' (folded)'}`}</h4>
        </div>
        <PlayerHand hand={data.hand} />
        <OptionsPanel options={options} callbacks={callbacks} />
    </div>
  );
};

export default PlayerDashboard;
