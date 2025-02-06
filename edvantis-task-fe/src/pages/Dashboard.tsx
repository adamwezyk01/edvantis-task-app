import React, { useState, useEffect } from 'react';
import {
  Table, TableHead, TableRow, TableCell, TableBody,
  Button, TextField, Box, FormControl, Select, MenuItem, InputLabel,
  Pagination
} from '@mui/material';
import { useAuth } from '../contexts/AuthContext';

type IncidentType = 'FIRE' | 'CRIME' | 'ACCIDENT';
type StatusType = 'OPEN' | 'CLOSED';

interface EmergencyCall {
  id?: number;
  callerName: string;
  position: string;
  incidentType: IncidentType;
  status: StatusType;
}

interface PageResponse {
  content: EmergencyCall[];
  totalPages: number;
  number: number; // current page number (zero-indexed)
}

const Dashboard: React.FC = () => {
  const { authHeader } = useAuth();
  const [calls, setCalls] = useState<EmergencyCall[]>([]);
  const [newCall, setNewCall] = useState<EmergencyCall>({
    callerName: '',
    position: '',
    incidentType: 'FIRE',
    status: 'OPEN'
  });
  const [editCall, setEditCall] = useState<EmergencyCall | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 10;

  const fetchCalls = async (page: number = 0) => {
    try {
      const response = await fetch(`http://localhost:8080/api/emergency-calls?page=${page}&size=${pageSize}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': authHeader || '',
        }
      });
      if (!response.ok) {
        throw new Error(`Error fetching calls: ${response.status}`);
      }
      const data: PageResponse = await response.json();
      setCalls(data.content || []);
      setCurrentPage(data.number);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (authHeader) {
      fetchCalls(currentPage);
    }
  }, [authHeader, currentPage]);

  const handleCreate = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/emergency-calls', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': authHeader || '',
        },
        body: JSON.stringify(newCall)
      });
      if (!response.ok) {
        throw new Error(`Error creating call: ${response.status}`);
      }
      await fetchCalls(currentPage);
      setNewCall({
        callerName: '',
        position: '',
        incidentType: 'FIRE',
        status: 'OPEN'
      });
    } catch (error) {
      console.error(error);
    }
  };

  const handleDelete = async (callId: number | undefined) => {
    if (!callId) return;
    try {
      const response = await fetch(`http://localhost:8080/api/emergency-calls/${callId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': authHeader || ''
        }
      });
      if (!response.ok) {
        throw new Error(`Error deleting call: ${response.status}`);
      }
      fetchCalls(currentPage);
    } catch (error) {
      console.error(error);
    }
  };

  const handleEdit = (call: EmergencyCall) => {
    setEditCall(call);
  };

  const handleUpdate = async () => {
    if (!editCall || !editCall.id) return;
    try {
      const response = await fetch(`http://localhost:8080/api/emergency-calls/${editCall.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': authHeader || '',
        },
        body: JSON.stringify(editCall)
      });
      if (!response.ok) {
        throw new Error(`Error updating call: ${response.status}`);
      }
      setEditCall(null);
      fetchCalls(currentPage);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Box>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Caller Name</TableCell>
            <TableCell>Position</TableCell>
            <TableCell>Incident Type</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {calls.map(call => (
            <TableRow key={call.id}>
              <TableCell>{call.callerName}</TableCell>
              <TableCell>{call.position}</TableCell>
              <TableCell>{call.incidentType}</TableCell>
              <TableCell>{call.status}</TableCell>
              <TableCell>
                <Box display="flex" gap={1}>
                  <Button variant="outlined" color="primary" onClick={() => handleEdit(call)}>
                    Edit
                  </Button>
                  <Button variant="outlined" color="error" onClick={() => handleDelete(call.id)}>
                    Delete
                  </Button>
                </Box>
              </TableCell>

            </TableRow>
          ))}
        </TableBody>
      </Table>

      {/* Pagination Controls */}
      <Box mt={2} display="flex" justifyContent="center">
        <Pagination
          count={totalPages}
          page={currentPage + 1}
          onChange={(_e, value) => setCurrentPage(value - 1)}
        />
      </Box>

      {/* New Call Form */}
      <Box mt={4}>
        <TextField
          label="Caller Name"
          value={newCall.callerName}
          onChange={e => setNewCall({ ...newCall, callerName: e.target.value })}
          sx={{ marginRight: 2 }}
        />
        <TextField
          label="Position"
          value={newCall.position}
          onChange={e => setNewCall({ ...newCall, position: e.target.value })}
          sx={{ marginRight: 2 }}
        />
        <FormControl sx={{ marginRight: 2, minWidth: 120 }}>
          <InputLabel id="incident-label">Incident Type</InputLabel>
          <Select
            labelId="incident-label"
            value={newCall.incidentType}
            label="Incident Type"
            onChange={e => setNewCall({ ...newCall, incidentType: e.target.value as IncidentType })}
          >
            <MenuItem value="FIRE">FIRE</MenuItem>
            <MenuItem value="CRIME">CRIME</MenuItem>
            <MenuItem value="ACCIDENT">ACCIDENT</MenuItem>
          </Select>
        </FormControl>
        <FormControl sx={{ marginRight: 2, minWidth: 120 }}>
          <InputLabel id="status-label">Status</InputLabel>
          <Select
            labelId="status-label"
            value={newCall.status}
            label="Status"
            onChange={e => setNewCall({ ...newCall, status: e.target.value as StatusType })}
          >
            <MenuItem value="OPEN">OPEN</MenuItem>
            <MenuItem value="CLOSED">CLOSED</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleCreate}>
          Add Emergency Call
        </Button>
      </Box>

      {/* Edit Call Form */}
      {editCall && (
        <Box mt={4}>
          <TextField
            label="Caller Name"
            value={editCall.callerName}
            onChange={e => setEditCall({ ...editCall, callerName: e.target.value })}
            sx={{ marginRight: 2 }}
          />
          <TextField
            label="Position"
            value={editCall.position}
            onChange={e => setEditCall({ ...editCall, position: e.target.value })}
            sx={{ marginRight: 2 }}
          />
          <FormControl sx={{ marginRight: 2, minWidth: 120 }}>
            <InputLabel id="edit-incident-label">Incident Type</InputLabel>
            <Select
              labelId="edit-incident-label"
              value={editCall.incidentType}
              label="Incident Type"
              onChange={e => setEditCall({ ...editCall, incidentType: e.target.value as IncidentType })}
            >
              <MenuItem value="FIRE">FIRE</MenuItem>
              <MenuItem value="CRIME">CRIME</MenuItem>
              <MenuItem value="ACCIDENT">ACCIDENT</MenuItem>
            </Select>
          </FormControl>
          <FormControl sx={{ marginRight: 2, minWidth: 120 }}>
            <InputLabel id="edit-status-label">Status</InputLabel>
            <Select
              labelId="edit-status-label"
              value={editCall.status}
              label="Status"
              onChange={e => setEditCall({ ...editCall, status: e.target.value as StatusType })}
            >
              <MenuItem value="OPEN">OPEN</MenuItem>
              <MenuItem value="CLOSED">CLOSED</MenuItem>
            </Select>
          </FormControl>
          <Button variant="contained" onClick={handleUpdate}>
            Save Changes
          </Button>
          <Button variant="text" onClick={() => setEditCall(null)}>
            Cancel
          </Button>
        </Box>
      )}
    </Box>
  );
};

export default Dashboard;
