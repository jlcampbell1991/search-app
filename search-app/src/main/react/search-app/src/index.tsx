import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import { useState } from 'react';

type Event = React.MouseEvent<HTMLInputElement, MouseEvent>;
type Element = JSX.Element;

function Form(props: { onClick: (e: Event, s: string) => void }): Element {
  function inputValue(): string {
    return $("#searchInput").val() as string;
  }

  return(
    <form>
      <input type="text" id="searchInput"></input>
      <input type="submit" value="Search" id="searchButton" onClick= {
        (e: Event) => {
          props.onClick(e, inputValue())
        }
      }/>
    </form>
  );
}

interface Response {
  result: string;
  url: string;
  source: string;
}

function Row(props: { data: Response }): Element {
  return (
    <tr>
      <td>{props.data.result}</td>
      <td><a href={props.data.url} target="_blank" rel="noreferrer">{props.data.source}</a></td>
    </tr>
  )
}

function Table(props: { display: string, responses: Array<Response> }): Element {
  const rows = props.responses.slice().map((resp, _) => {
    return (
      <Row data={resp} key={resp.result} />
    )
  })

  return(
    <table id="searches" style={{display: props.display}}>
      <tbody>
        <tr key="searchesHeader">
          <th>Search</th>
          <th>Result</th>
        </tr>
        {rows}
      </tbody>
    </table>
  );
}

function Searching(props: { display: string }): Element {
  return(
    <div id="searching" style={{display: props.display}}>Searching...</div>
  );
}

const Page: () => Element = () => {
  const [responses, setResponses] = useState<Array<Response>>([]);
  const [tableDisplay, setTableDisplay] = useState<string>("none");
  const [searchDisplay, setSearchDisplay] = useState<string>("none");

  function handleClick(responses: Array<Response>, event: Event, req: string) {
    event.preventDefault();
    $("#searching").show();

    $.ajax({
        type: 'GET',
        url: `http://localhost:8080/${req}`,
        success: function(data) {
          setResponses([{
            result: req,
            url: data.AbstractURL,
            source: data.AbstractSource
          }].concat(responses));
          setTableDisplay("");
          setSearchDisplay("none");
        },
        complete: function() {
            $("#searching").hide();
        },
        error: function(_, status, error) {
            alert(status + " - " + error);
        }
    });
  }

  return(
    <div>
      <Form onClick={ (e, r) => handleClick(responses, e, r) }/>
      <Table display={tableDisplay} responses={responses} />
      <Searching display={searchDisplay} />
    </div>
  )
}

ReactDOM.render(
  <Page />,
  document.getElementById('root')
);
