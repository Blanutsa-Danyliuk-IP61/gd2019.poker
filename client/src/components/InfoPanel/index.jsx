import React  from 'react';

import './styles.css';

const InfoPanel = (props) => {

  return (
    <div className='info-panel'>
        {
          props.messages.map((str, i) => (
            <p key={str.substring(0, 3) + str.substring(str.length - 3, str.length)}>{str}</p>
          ))
        }
    </div>
  );
};

export default InfoPanel;
